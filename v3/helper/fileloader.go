package helper

import (
	"errors"
	"github.com/davyxu/tabtoy/v3/report"
	"path/filepath"
	"sync"
)

type FileGetter interface {
	GetFile(filename string) (TableFile, error)
}

type FileLoader struct {
	fileByName sync.Map
	inputFile  []string

	syncLoad bool
}

func (self *FileLoader) AddFile(filename string) {

	self.inputFile = append(self.inputFile, filename)
}

func (self *FileLoader) Commit() {

	var task sync.WaitGroup
	task.Add(len(self.inputFile))

	for _, inputFileName := range self.inputFile {

		go func(fileName string) {

			self.fileByName.Store(fileName, loadFileByExt(fileName))

			task.Done()

		}(inputFileName)

	}

	task.Wait()

	self.inputFile = self.inputFile[0:0]
}

func loadFileByExt(filename string) interface{} {

	var tabFile TableFile
	switch filepath.Ext(filename) {
	case ".xlsx", ".xls", ".xlsm":

		tabFile = NewXlsxFile()

	case ".csv":
		tabFile = NewCSVFile()

	default:
		report.ReportError("UnknownInputFileExtension", filename)
	}

	err := tabFile.Load(filename)

	if err != nil {
		return err
	}

	return tabFile
}

func (self *FileLoader) GetFile(filename string) (TableFile, error) {

	if self.syncLoad {

		result := loadFileByExt(filename)
		if err, ok := result.(error); ok {
			return nil, err
		}

		return result.(TableFile), nil

	} else {
		if result, ok := self.fileByName.Load(filename); ok {

			if err, ok := result.(error); ok {
				return nil, err
			}

			return result.(TableFile), nil

		} else {
			return nil, errors.New("not found")
		}
	}

}

func NewFileLoader(syncLoad bool) *FileLoader {
	return &FileLoader{
		syncLoad: syncLoad,
	}
}
