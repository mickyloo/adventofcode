use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "04";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn check_xmas(buffer: &mut Vec<char>) -> i32 {
        if let Some(s) = buffer.get(0..4) {
            let word = s.iter().join("");
            if word == "XMAS" || word == "SAMX" {
                return 1;
            }
        }
        0
    }

    fn part1<R: BufRead>(reader: R) -> Result<i32> {
        let grid: Vec<Vec<_>> = reader
            .lines()
            .map(|line| line.unwrap().chars().collect::<Vec<_>>())
            .collect();

        let mut answer = 0;
        let size = grid.len();
        //rows and cols
        for i in 0..size {
            let mut buffer_row = Vec::new();
            let mut buffer_col = Vec::new();
            for j in 0..size {
                buffer_row.insert(0, grid[i][j]);
                buffer_col.insert(0, grid[j][i]);
                answer += check_xmas(&mut buffer_row);
                answer += check_xmas(&mut buffer_col);
            }
        }
        //diags
        for k in 0..size * 2 {
            let mut buffer_asc = Vec::new();
            let mut buffer_desc = Vec::new();
            for j in 0..k + 1 {
                let i = k - j;
                if i < size && j < size {
                    buffer_asc.insert(0, grid[i][j]);
                    buffer_desc.insert(0, grid[size - 1 - i][j]);
                    answer += check_xmas(&mut buffer_asc);
                    answer += check_xmas(&mut buffer_desc);
                }
            }
        }

        Ok(answer)
    }

    assert_eq!(18, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let grid: Vec<Vec<_>> = reader
            .lines()
            .map(|line| line.unwrap().chars().collect::<Vec<_>>())
            .collect();

        let mut answer = 0;
        let size = grid.len();
        for i in 0..size {
            for j in 0..size {
                let character = grid[i][j];
                if character != 'A' {
                    continue;
                }

                let mut buffer = Vec::new();
                //check corners
                let _i = i as i32;
                let _j = j as i32;
                let _size = size as i32;
                if (_i - 1 >= 0) && (_i - 1 < _size) && (_j - 1 >= 0) && (_j - 1 < _size) {
                    buffer.push(grid[i - 1][j - 1]);
                }
                if (_i - 1 >= 0) && (_i - 1 < _size) && (_j + 1 >= 0) && (_j + 1 < _size) {
                    buffer.push(grid[i - 1][j + 1]);
                }
                if (_i + 1 >= 0) && (_i + 1 < _size) && (_j - 1 >= 0) && (_j - 1 < _size) {
                    buffer.push(grid[i + 1][j - 1]);
                }
                if (_i + 1 >= 0) && (_i + 1 < _size) && (_j + 1 >= 0) && (_j + 1 < _size) {
                    buffer.push(grid[i + 1][j + 1]);
                }

                let word = buffer.iter().join("");
                if word == "MMSS" || word == "MSMS" || word == "SMSM" || word == "SSMM" {
                    answer += 1;
                }
            }
        }
        Ok(answer)
    }

    assert_eq!(9, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
